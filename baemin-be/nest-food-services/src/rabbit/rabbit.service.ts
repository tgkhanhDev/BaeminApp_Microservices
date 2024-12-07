import { Injectable, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import * as amqp from 'amqplib';

@Injectable()
export class RabbitMQService implements OnModuleInit, OnModuleDestroy {
    private connection: amqp.Connection;
    private channel: amqp.Channel;

    async onModuleInit() {
        await this.connectRabbitMQ();
    }

    async onModuleDestroy() {
        // Gracefully close the channel and connection
        if (this.channel) {
            await this.channel.close();
            console.log('RabbitMQ channel closed.');
        }
        if (this.connection) {
            await this.connection.close();
            console.log('RabbitMQ connection closed.');
        }
    }

    private async connectRabbitMQ() {
        try {
            // Establish connection to RabbitMQ
            this.connection = await amqp.connect('amqp://admin:170504@localhost:5672');

            // Create a channel
            this.channel = await this.connection.createChannel();

            console.log('RabbitMQ connected and channel created.');
        } catch (error) {
            console.error('Error connecting to RabbitMQ:', error);
            throw error;
        }
    }

    async consume(queue: string, onMessage: (msg: amqp.ConsumeMessage | null) => void) {
        await this.connectRabbitMQ();

        // Ensure the queue exists
        await this.channel.assertQueue(queue, {
            durable: true,
            arguments: {
                'x-dead-letter-exchange': 'dead_letter_exchange', // Add the same argument
                'x-dead-letter-routing-key': 'dead_letter_routing_key', // Add the same argument
            },
        });
        // Consume messages from the queue
        this.channel.consume(queue, (msg) => {
            onMessage(msg);
            if (msg) {
                // Acknowledge the message after processing
                this.channel.ack(msg);
            }
        });

        console.log(`Listening for messages on queue: ${queue}`);
    }

    public getChannel() {
        return this.channel;
    }
}
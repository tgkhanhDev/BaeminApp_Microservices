import { Injectable, OnModuleDestroy, OnModuleInit } from '@nestjs/common';
import * as amqp from 'amqplib';
import { plainToInstance } from 'class-transformer';
import { validate } from 'class-validator';

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


    public sendResponse(msg: amqp.ConsumeMessage, response: any) {
        // Extract the correlationId and replyTo properties from the message
        const correlationId = msg.properties.correlationId;
        const replyQueue = msg.properties.replyTo;
        console.log("res to send: ", response);
        
        // Send the response back to the queue specified by replyTo
        this.getChannel().sendToQueue(replyQueue, Buffer.from(JSON.stringify(response)), {
            correlationId: correlationId, // Associate the response with the original request
            replyTo: replyQueue // Optional: Include the reply queue again (usually not necessary)
        });

    }

    public async parseJsonToDto<T>(jsonString: string, dtoClass: new () => T): Promise<T | null> {
        try {
            // Parse the JSON string
            const plainObject = JSON.parse(jsonString);

            // Transform to DTO instance
            const dtoInstance = plainToInstance(dtoClass, plainObject);

            // Validate the DTO instance
            const errors = await validate(dtoInstance as object);

            if (errors.length > 0) {
                console.error('Validation errors:', errors);
                return null; // Return null if validation fails
            }

            return dtoInstance; // Return the valid DTO instance
        } catch (error) {
            console.error('Error parsing JSON:', error);
            return null; // Return null if parsing fails
        }
    }
}
import { Controller, Get } from '@nestjs/common';
import { AppService } from './app.service';
import { Ctx, MessagePattern, Payload, RmqContext } from '@nestjs/microservices';

@Controller()
export class AppController {
  constructor(private readonly appService: AppService) {}

  @MessagePattern('shop_food_queue') // Matches the queue name
  async handleMessage(@Payload() data: any, @Ctx() context: RmqContext) {
    const channel = context.getChannelRef();
    const message = context.getMessage();

    console.log('Received Message:', data);

    // Acknowledge the message
    channel.ack(message);

    return { status: 'success', data };
  }
}
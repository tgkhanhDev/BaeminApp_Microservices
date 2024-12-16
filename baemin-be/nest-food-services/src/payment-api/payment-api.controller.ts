import { Body, Controller, Get, Param, Patch, Post, ValidationPipe } from '@nestjs/common';
import { PaymentService } from './payment-api.service';
import { ApiBody, ApiOperation, ApiParam, ApiResponse } from '@nestjs/swagger';
import { CreatePaymentDto } from './dto/request/create-payment.dto';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';

@Controller('payment')
export class PaymentApiController {
  private readonly paymentQueue = 'nest_payment_service';

  constructor(private readonly paymentService: PaymentService,
    private readonly rabbitMQService: RabbitMQService
  ) { }

  async onModuleInit() {
    // Listen to the queue
    this.rabbitMQService.consume(this.paymentQueue, this.paymentQueueProducer.bind(this));
  }

  private async paymentQueueProducer(msg: ConsumeMessage) {
    if (msg) {
      const routingKey = msg.fields.routingKey;
      let res = null;
      if (routingKey == 'payment.create-payment') {
        const createPaymentDto = JSON.parse(msg.content.toString());
        let payload = null;
        if (createPaymentDto != null) {
          payload = await this.rabbitMQService.parseJsonToDto(createPaymentDto, CreatePaymentDto)
        }
        res = await this.paymentService.createPayment(payload);
      } else if (routingKey == 'payment.find-by-user-id') {
        const userId = JSON.parse(msg.content.toString());
        res = await this.paymentService.findAllByaccountId(userId);
      } else if (routingKey == 'payment.pay-for-payment') {
        const paymentId = JSON.parse(msg.content.toString());
        res = await this.paymentService.payForPayment(paymentId);
      }
      
      console.log("PaymentRes: ", res);
      this.rabbitMQService.sendResponse(msg, res);

    }
  }

}

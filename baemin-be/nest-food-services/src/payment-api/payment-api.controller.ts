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
        console.log("payloadPayment1: ", createPaymentDto);

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


  // @Get(":account_id")
  // @ApiOperation({
  //   summary: 'Get all payment by UserId',
  //   description: '',
  // })
  // @ApiParam({
  //   name: 'account_id',
  //   type: String,
  //   required: true
  // })
  // async findPaymentsByUserId(@Param('account_id') account_id: string) {
  //   const res = await this.paymentApiService.findAllByaccountId(account_id);
  //   return res;
  // }

  // @Post('')
  // @ApiOperation({
  //   summary: 'Paying cart/ buy now',
  // })
  // @ApiBody({
  //   description: 'Create payment request body',
  //   type: CreatePaymentDto,
  //   examples: {
  //     example1: {
  //       summary: 'Transaction Example',
  //       value: {
  //         "delivery_address": "123 Main St",
  //         "message": "Your order is confirmed!",
  //         "account_id": "",
  //         // "status": "Unpaid",
  //         "transactions": [
  //           {
  //             // "transaction_id": "generated-id-1",
  //             "food_name": "Latte",
  //             "food_id": "33b92b28-914f-4ead-a6b4-f90134b2ec3c",
  //             "per_price": 50,
  //             "type": "combo",
  //             "food_thumbnail": "https://images.unsplash.com/photo-1588195538326-c5b1e9f80a1b?q=80&w=1950&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
  //             "quantity": 5,
  //             "shop_id": "a14f9c18-56f0-4a52-8e62-c1524ec0e8e9"
  //           },
  //           {
  //             // "transaction_id": "generated-id-2",
  //             "food_id": "3f53a922-abd6-465d-bfbf-d02804ae3bda",
  //             "food_name": "Pho",
  //             "per_price": 150,
  //             "type": "sale",
  //             "food_thumbnail": "https://images.unsplash.com/photo-1588195538326-c5b1e9f80a1b?q=80&w=1950&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
  //             "quantity": 5,
  //             "shop_id": "668c4b6e-872d-4d9e-bc2c-3e17b58f8fd5"
  //           }
  //         ]
  //       }
  //     },
  //   },
  // })
  // @ApiResponse({ status: 201, description: 'The payment has been successfully created.' })
  // @ApiResponse({ status: 400, description: 'Invalid input, object invalid.' })
  // @ApiResponse({ status: 500, description: 'Internal server error.' })
  // async createPayment(@Body(new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true })) req: CreatePaymentDto) {
  //   const res = await this.paymentApiService.createPayment(req);
  //   return res;
  // }

  // @Patch(':payment_id')
  // @ApiOperation({
  //   summary: 'Pay for payment',
  //   description: 'This action will also deduct the amount in food stock',
  // })
  // @ApiParam({
  //   name: 'payment_id',
  //   description: 'Payment id',
  //   required: true,
  //   type: String
  // })
  // async payForPayment(@Param('payment_id') payment_id: string) {
  //   const res = await this.paymentApiService.payForPayment(payment_id);
  //   return res;
  // }
}

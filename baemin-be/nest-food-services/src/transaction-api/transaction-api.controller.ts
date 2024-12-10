import { Body, Controller, Get, Post, Query, ValidationPipe } from '@nestjs/common';
import { TransactionService } from './transaction-api.service';
import { ApiBody, ApiOperation, ApiParam, ApiQuery, ApiResponse } from '@nestjs/swagger';
import { CreateTransactionDto, FilterTransactionDto } from './dto/request/create-transaction.dto';
import { TransactionResponseDto } from './dto/response/response-transaction.dto';
import { transaction_status } from './entities/transaction.entity';
import { food_type } from '.prismas/client-postgres';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';

@Controller('transaction')
export class TransactionApiController {

  private readonly transactionQueue = 'nest_transaction_service';

  constructor(private readonly transactionService: TransactionService,
    private readonly rabbitMQService: RabbitMQService
  ) { }

  async onModuleInit() {
    // Listen to the queue
    this.rabbitMQService.consume(this.transactionQueue, this.transactionQueueProducer.bind(this));
  }


  private async transactionQueueProducer(msg: ConsumeMessage) {
    if (msg) {
      const routingKey = msg.fields.routingKey;
      let res = null;
      if (routingKey == 'transaction.get-all-filtered') {
        const transactionResponseDto = JSON.parse(msg.content.toString());
        // console.log("hihi: ", transactionResponseDto);
        
        let payload = null;
        if (transactionResponseDto != null) {
          payload = await this.rabbitMQService.parseJsonToDto(transactionResponseDto, TransactionResponseDto)
        }
        res = await this.transactionService.getTransactionsWithFilter(payload);
      } else if (routingKey == 'transaction.create-request') {
        const createTransactionDto = JSON.parse(msg.content.toString());
        // console.log("hihi1: ", createTransactionDto);

        let payload = null;
        if (createTransactionDto != null) {
          payload = await this.rabbitMQService.parseJsonToDto(createTransactionDto, CreateTransactionDto)
        }

        res = await this.transactionService.createTransaction(payload);
      }
      // console.log("transRes: ", res);
      
      this.rabbitMQService.sendResponse(msg, res);

    }
  }

  @Get('')
  @ApiOperation({
    summary: 'Get all transactions',
    description: 'Fetch all transactions with optional filters for transaction ID, payment ID, or status.',
  })
  @ApiQuery({
    name: 'transaction_id',
    description: 'Filter by transaction ID (Ex: 4115cfee-6534-430d-8d06-8741ad26cc8c)',
    required: false,
  })
  @ApiQuery({
    name: 'payment_id',
    description: 'Filter by payment ID (Ex: 65c8753d-2389-40db-ae8d-912fc9450640)',
    required: false,
  })
  @ApiQuery({
    name: 'status',
    description: 'Filter by transaction status',
    enum: transaction_status,
    required: false,
  })
  @ApiResponse({
    status: 200,
    description: 'Successfully fetched filtered transactions',
    type: [TransactionResponseDto],
    examples: {
      example1: {
        summary: 'Example Transaction Response',
        value: [
          {
            transaction_id: '1f908569-8bad-4cff-8911-811df6c34a0a',
            food_id: '33b92b28-914f-4ead-a6b4-f90134b2ec3c',
            food_name: 'Latte',
            food_thumbnail: 'https://images.unsplash.com/photo-1588195538326-c5b1e9f80a1b?q=80&w=1950&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
            shop_id: 'a14f9c18-56f0-4a52-8e62-c1524ec0e8e9',
            type: 'combo',
            quantity: 2,
            per_price: 50,
            status: 'complete',
            Payment: '33b92b28-914f-4ead-a6b4-f90134b2ec3c',
          },
        ],
      },
    },
  })
  async findTransactions(@Query() filter: FilterTransactionDto): Promise<TransactionResponseDto[]> {
    const res = await this.transactionService.getTransactionsWithFilter(filter);
    return res;
  }


  //Not yet handle 400 for invalid input
  @Post('')
  @ApiOperation({
    summary: 'Create Single Transaction with no payment',
  })
  @ApiBody({
    description: 'Create transaction request body',
    type: CreateTransactionDto,
    examples: {
      example1: {
        summary: 'Transaction Example',
        value:
        {
          food_id: "33b92b28-914f-4ead-a6b4-f90134b2ec3c",
          food_name: 'Macchiato1',
          per_price: 501,
          type: food_type.bubble_tea,
          food_thumbnail: 'https://down-vn.img.susercontent.com/vn-11134513-7r98o-lsu9pjg9gdcp31@resize_ss640x400',
          quantity: 10,
          payment_id: null, // can have payment_id as UUID here
          shop_id: '937f6543-a21b-34c5-d678-123456789abc'
        }
        ,
      },
    },
  })
  @ApiResponse({ status: 201, description: 'The transaction has been successfully created.' })
  @ApiResponse({ status: 400, description: 'Invalid input, object invalid.' })
  @ApiResponse({ status: 500, description: 'Internal server error.' })
  async createTransaction(@Body(new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true })) req: CreateTransactionDto) {
    const res = await this.transactionService.createTransaction(req);
    return res;
  }

}

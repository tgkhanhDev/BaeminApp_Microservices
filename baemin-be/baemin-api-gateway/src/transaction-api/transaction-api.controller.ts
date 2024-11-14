import { Body, Controller, Get, Post, Query } from '@nestjs/common';
import { TransactionApiService } from './transaction-api.service';
import { ApiBody, ApiOperation, ApiParam, ApiQuery, ApiResponse } from '@nestjs/swagger';
import { CreateTransactionDto, FilterTransactionDto } from './dto/request/create-transaction.dto';
import { ResponseTransactionDto } from './dto/response/response-transaction.dto';
import { transaction_status } from './entities/transaction.entity';

@Controller('transaction')
export class TransactionApiController {
  constructor(private readonly transactionApiService: TransactionApiService) { }

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
    type: [ResponseTransactionDto],
  })
  async findTransactions(@Query() filter: FilterTransactionDto): Promise<ResponseTransactionDto[]> {
    const res = await this.transactionApiService.getTransactionWithFilter(filter);
    return res;
  }


  //Not yet handle 400 for invalid input
  @Post('')
  @ApiOperation({
    summary: 'Get all shops by label',
  })
  @ApiBody({
    description: 'Create transaction request body',
    type: CreateTransactionDto,
    examples: {
      example1: {
        summary: 'Transaction Example',
        value: {
          food_id: '1000f408-079e-45dc-9e58-a725e44a1b86',
          quantity: 10,
          per_price: 500,
          payment_id: '987e6543-e21b-34c5-d678-123456789abc',
          status: 'in_progress',
        },
      },
    },
  })
  @ApiResponse({ status: 201, description: 'The transaction has been successfully created.' })
  @ApiResponse({ status: 400, description: 'Invalid input, object invalid.' })
  @ApiResponse({ status: 500, description: 'Internal server error.' })
  async createTransaction(@Body() req: CreateTransactionDto) {    
    const res = await this.transactionApiService.createTransaction(req);    
    return res;
  }

}

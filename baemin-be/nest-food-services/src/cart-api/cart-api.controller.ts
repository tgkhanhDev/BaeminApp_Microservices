import { Body, Controller, Delete, Get, Param, Patch, Post, Query, ValidationPipe } from '@nestjs/common';
import { CartApiService } from './cart-api.service';
import { ApiBody, ApiOperation, ApiParam, ApiQuery } from '@nestjs/swagger';
import { AddCartItemRequestDto, UpdateCartItemRequestDto } from './dto/request/cart-request.dto';
import { RabbitMQService } from 'src/rabbit/rabbit.service';
import { ConsumeMessage } from 'amqplib';
import { plainToInstance } from 'class-transformer';
import { validate } from 'class-validator';

@Controller('cart-api')
export class CartApiController {

  private readonly cartQueue = 'nest_cart_service';

  constructor(private readonly cartApiService: CartApiService,
    private readonly rabbitMQService: RabbitMQService
  ) { }

  async onModuleInit() {
    // Listen to the queue
    this.rabbitMQService.consume(this.cartQueue, this.cartQueueProducer.bind(this));
  }

  private async cartQueueProducer(msg: ConsumeMessage) {
    if (msg) {
      const routingKey = msg.fields.routingKey;
      let res = null;
      if (routingKey == 'cart.get-all-by-user-id') {
        let userId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.cartApiService.findCartItemByUserId(userId);
      } else if (routingKey == 'cart.add-item') {
        const addCartItemRequestDto = JSON.parse(msg.content.toString());

        let payload = null;
        if (addCartItemRequestDto != null) {
          payload = await this.parseJsonToDto(addCartItemRequestDto, AddCartItemRequestDto)
        }

        res = await this.cartApiService.addItemToCart(payload);
      } else if (routingKey == 'cart.delete-item') {
        let cartId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.cartApiService.deleteCartItem(cartId);
      } else if (routingKey == 'cart.empty-cart') {
        let userId = msg.content.toString().replace(/^"|"$/g, '');
        res = await this.cartApiService.emptyCartItem(userId);
      }
      this.rabbitMQService.sendResponse(msg, res);

    }
  }

  async parseJsonToDto<T>(jsonString: string, dtoClass: new () => T): Promise<T | null> {
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

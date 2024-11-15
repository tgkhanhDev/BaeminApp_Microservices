import { Injectable } from '@nestjs/common';
import { PrismaPostgresService } from 'src/prisma/prisma.service';

@Injectable()
export class PaymentService {
    constructor(
        private postgresDAO: PrismaPostgresService
    ) {}

    // async createPayment()
}

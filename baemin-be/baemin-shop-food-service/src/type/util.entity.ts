import { ApiProperty } from "@nestjs/swagger";

export class ApiUtilResponse<T> {
    @ApiProperty({ description: 'Code of the response', example: 200 })
    code?: number;
    @ApiProperty({ description: 'Message of the response', example: "Success" })
    message?: string;

    data?: T;
}
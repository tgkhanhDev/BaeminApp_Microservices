import { Global, Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { ElasticsearchModule } from '@nestjs/elasticsearch';

@Global()
@Module({
    imports: [
        ElasticsearchModule.registerAsync({
            imports: [ConfigModule],
            useFactory: async (configService: ConfigService) => ({
                // node: configService.get("ELASTICSEARCH_NODE"), //? For using .env
                node: "https://localhost:9200",
                auth: {
                    username: "elastic",
                    password: "170504"
                },
                tls: {
                    rejectUnauthorized: false
                }
            }),
            inject: [ConfigService]
        })
    ],
    exports: [ElasticsearchModule]
})
export class ElasticModule { }

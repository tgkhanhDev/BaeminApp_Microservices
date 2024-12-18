import { Inject, Injectable, Logger } from '@nestjs/common';
import { lastValueFrom } from 'rxjs';
import { ShopResponseDetailDto, ShopResponseDto } from './dto/shops-response.dto';
import { ShopFilterRequestDto } from './dto/shop-request.dto';
import { PrismaPostgresService } from 'src/prisma/prisma.service';
import { ElasticsearchService } from '@nestjs/elasticsearch';
import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Cache } from 'cache-manager';

@Injectable()
export class ShopApiService {
    constructor(
        private postgresDAO: PrismaPostgresService,
        private elasticsearchService: ElasticsearchService,
        @Inject(CACHE_MANAGER) private cacheManager: Cache
    ) { }

    async findAllShopFilter(shopFilterRequestDto: ShopFilterRequestDto): Promise<ShopResponseDto[]> {

        console.log("shopFilterRequestDto: ", shopFilterRequestDto);

        //Xu ly cache
        if ((!shopFilterRequestDto) || (shopFilterRequestDto && shopFilterRequestDto.label == "" && shopFilterRequestDto.location == "" )) {
            let shopNoFilterDataCache: ShopResponseDto[] = await this.cacheManager.get("shop-get-all-filtered");
            if (shopNoFilterDataCache) {
                console.log("hi I'm from Cache");
                return shopNoFilterDataCache;
            }

            let result = await this.elasticsearchService.search({
                index: "shop", // Your index
            });

            const shopNoFilterData: ShopResponseDto[] = result.hits.hits.map((hit: any) => ({
                shop_id: hit._source?.shop_id || '',
                shop_name: hit._source?.shop_name || '',
                shop_address: hit._source?.shop_address || '',
                shop_thumbnail: hit._source?.shop_thumbnail || '',
                category: hit._source?.category || '',
                label: hit._source?.label || '',
                location: hit._source?.location || '',
                rating: hit._source?.rating || 0,
                open_time: hit._source?.open_time || '',
                close_time: hit._source?.close_time || '',
                price_start: hit._source?.price_start || 0,
                price_end: hit._source?.price_end || 0,
                is_open: hit._source?.is_open || false,
            }));

            await this.cacheManager.set("shop-get-all-filtered", shopNoFilterData);
            return shopNoFilterData; // Return all shops if no filter is provided
        }

        const { label, location, name } = shopFilterRequestDto;

        const query: any = {
            bool: {
                must: [],
                filter: [],
            },
        };

        if (label) {
            query.bool.must.push({
                match: { label },
            });
        }

        if (location) {
            query.bool.must.push({
                match: { location },
            });
        }

        if (name) {
            query.bool.must.push({
                match: { shop_name: { query: name, operator: "and" } },
            });
        }

        // Specify the fields you want to retrieve
        const sourceFields = [
            "shop_id",
            "shop_name",
            "shop_address",
            "shop_thumbnail",
            "category",
            "label",
            "location",
            "rating",
            "open_time",
            "close_time",
            "price_start",
            "price_end",
            "is_open",
        ];

        // Send the query to Elasticsearch
        const result = await this.elasticsearchService.search({
            index: "shop", // Your index
            body: {
                query,
                _source: sourceFields,
            },
        });

        // Map the search hits to ShopResponseDto
        const shops: ShopResponseDto[] = result.hits.hits.map((hit: any) => ({
            shop_id: hit._source?.shop_id || '',
            shop_name: hit._source?.shop_name || '',
            shop_address: hit._source?.shop_address || '',
            shop_thumbnail: hit._source?.shop_thumbnail || '',
            category: hit._source?.category || '',
            label: hit._source?.label || '',
            location: hit._source?.location || '',
            rating: hit._source?.rating || 0,
            open_time: hit._source?.open_time || '',
            close_time: hit._source?.close_time || '',
            price_start: hit._source?.price_start || 0,
            price_end: hit._source?.price_end || 0,
            is_open: hit._source?.is_open || false,
        }));

        return shops;


        // return await this.postgresDAO.shop.findMany({
        //     where: {
        //         ...(label ? { label } : {}),  // Include 'label' only if it is not empty or undefined
        //         ...(location ? { location } : {}), // Include 'location' only if it is not empty or undefined
        //         ...(name ? { shop_name: { contains: name } } : {}), // Include 'name' only if it is not empty or undefined
        //     },
        //     select: {
        //         shop_id: true,
        //         shop_name: true,
        //         shop_address: true,
        //         shop_thumbnail: true,
        //         category: true,
        //         label: true,
        //         location: true,
        //         rating: true,
        //         open_time: true,
        //         close_time: true,
        //         price_start: true,
        //         price_end: true,
        //         is_open: true,
        //     },
        // });
    }

    findShopById(shop_id: string): Promise<any> {
        return this.postgresDAO.shop.findUniqueOrThrow({
            where: {
                shop_id
            },
            select: {
                shop_id: true,
                shop_name: true,
                shop_address: true,
                shop_thumbnail: true,
                category: true,
                label: true,
                location: true,
                rating: true,
                open_time: true,
                close_time: true,
                price_start: true,
                price_end: true,
                is_open: true,
                food: {
                    select: {
                        food_id: true,
                        food_name: true,
                        description: true,
                        price: true,
                        type: true,
                        food_thumbnail: true,
                        shop: false,
                        shop_id: false
                    }
                }
            }
        })
    }


}

import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Inject, Injectable } from '@nestjs/common';
import { Cache } from 'cache-manager';

@Injectable()
export class CacheManagerService {
    constructor(@Inject(CACHE_MANAGER) private cacheManager: Cache) {
        
     }
    async getFromCache(key: string): Promise<any> {
        console.log("cache: ", key, typeof key);
        
        return await this.cacheManager.get(key); // Retrieve value from cache
    }

    async setToCache(key: string, value: any): Promise<void> {
        await this.cacheManager.set(key, value); // Set value with a TTL of 300 seconds
    }

    async deleteFromCache(key: string): Promise<void> {
        await this.cacheManager.del(key); // Delete value from cache
    }
}

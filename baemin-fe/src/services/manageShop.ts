import { apiInstance } from "../constant/apiInstance";
import { Shop, ShopDetail, FilterParams } from "../types/shop";
import { utilsResponse } from "../types/utils";

const api = apiInstance("http://localhost:8080/shop-api");

export const manageShop = {
  getShop: (params: FilterParams = {}) => {
    const queryParams = new URLSearchParams();
    if (params.name) queryParams.append('name', params.name);
    if (params.location) queryParams.append('location', params.location);
    if (params.label) queryParams.append('Label', params.label);    
    
    return api.get<utilsResponse<Shop>>(`/get-all?${queryParams.toString()}`);
  },
  getShopDetail: (payload: string) =>
    api.get<ShopDetail>(`/${payload}`),
};

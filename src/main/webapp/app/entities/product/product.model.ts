import { IProductCategory } from 'app/entities/product-category/product-category.model';

export interface IProduct {
  id?: number;
  name?: string;
  description?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  productCategory?: IProductCategory | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public productCategory?: IProductCategory | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}

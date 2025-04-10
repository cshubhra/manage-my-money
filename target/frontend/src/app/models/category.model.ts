export interface Category {
    id?: number;
    name: string;
    description: string;
    active: boolean;
    createdAt?: Date;
    updatedAt?: Date;
    parentId?: number;
    slug?: string;
    imageUrl?: string;
    sortOrder?: number;
    metadata?: Record<string, any>;
    subCategories?: Category[];
}
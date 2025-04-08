export interface Transfer {
    id?: number;
    transferDate: Date;
    totalAmount: number;
    status: string;
    createdBy: string;
    createdAt?: Date;
    updatedAt?: Date;
    items: TransferItem[];
}

export interface TransferItem {
    id?: number;
    description: string;
    amount: number;
    quantity: number;
    categoryId: number;
}

export interface TransferFormData {
    transferDate: Date;
    items: TransferItemFormData[];
}

export interface TransferItemFormData {
    description: string;
    amount: number;
    quantity: number;
    categoryId: number;
}
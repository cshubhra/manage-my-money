export interface Goal {
    id?: number;
    title: string;
    description: string;
    targetAmount: number;
    currentAmount: number;
    startDate: Date;
    endDate: Date;
    status: GoalStatus;
    progress: number;
    category: string;
}

export enum GoalStatus {
    IN_PROGRESS = 'IN_PROGRESS',
    COMPLETED = 'COMPLETED',
    ON_HOLD = 'ON_HOLD',
    CANCELLED = 'CANCELLED'
}
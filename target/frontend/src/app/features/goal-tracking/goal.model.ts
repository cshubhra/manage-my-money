export interface Goal {
  id?: number;
  title: string;
  description: string;
  targetAmount: number;
  currentAmount: number;
  startDate: Date;
  endDate: Date;
  status: GoalStatus;
  category: string;
  currency: string;
  isCyclical: boolean;
  cyclePeriod?: string;
}

export enum GoalStatus {
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

export interface GoalProgress {
  id?: number;
  goalId: number;
  amount: number;
  date: Date;
  notes?: string;
}
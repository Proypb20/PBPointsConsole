import { IEvent } from 'app/entities/event/event.model';

export interface ICategory {
  id?: number;
  name?: string;
  gameTimeType?: string;
  gameTime?: number;
  stopTimeType?: string;
  stopTime?: number | null;
  stopSdTimeType?: string;
  stopSdTime?: number;
  points?: number | null;
  difPoints?: number;
  event?: IEvent;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public gameTimeType?: string,
    public gameTime?: number,
    public stopTimeType?: string,
    public stopTime?: number | null,
    public stopSdTimeType?: string,
    public stopSdTime?: number,
    public points?: number | null,
    public difPoints?: number,
    public event?: IEvent
  ) {}
}

export function getCategoryIdentifier(category: ICategory): number | undefined {
  return category.id;
}

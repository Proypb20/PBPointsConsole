import { ITournament } from 'app/entities/tournament/tournament.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IEvent {
  id?: number;
  name?: string | null;
  status?: Status | null;
  tournament?: ITournament;
}

export class Event implements IEvent {
  constructor(public id?: number, public name?: string | null, public status?: Status | null, public tournament?: ITournament) {}
}

export function getEventIdentifier(event: IEvent): number | undefined {
  return event.id;
}

export interface ITournament {
  id?: number;
  name?: string;
}

export class Tournament implements ITournament {
  constructor(public id?: number, public name?: string) {}
}

export function getTournamentIdentifier(tournament: ITournament): number | undefined {
  return tournament.id;
}

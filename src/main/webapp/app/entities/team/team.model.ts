export interface ITeam {
  id?: number;
  name?: string;
  logoContentType?: string | null;
  logo?: string | null;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public logoContentType?: string | null, public logo?: string | null) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}

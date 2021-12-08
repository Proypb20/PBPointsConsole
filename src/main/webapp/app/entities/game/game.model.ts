import { ITeam } from 'app/entities/team/team.model';
import { ICategory } from 'app/entities/category/category.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IGame {
  id?: number;
  gameNum?: number | null;
  clasif?: string | null;
  splitdeckNum?: number | null;
  pointsA?: number | null;
  pointsB?: number | null;
  overA?: number | null;
  overB?: number | null;
  pvpA?: number | null;
  pvpB?: number | null;
  timeLeft?: number | null;
  status?: Status | null;
  teamA?: ITeam;
  teamB?: ITeam;
  category?: ICategory;
}

export class Game implements IGame {
  constructor(
    public id?: number,
    public gameNum?: number | null,
    public clasif?: string | null,
    public splitdeckNum?: number | null,
    public pointsA?: number | null,
    public pointsB?: number | null,
    public overA?: number | null,
    public overB?: number | null,
    public pvpA?: number | null,
    public pvpB?: number | null,
    public timeLeft?: number | null,
    public status?: Status | null,
    public teamA?: ITeam,
    public teamB?: ITeam,
    public category?: ICategory
  ) {}
}

export function getGameIdentifier(game: IGame): number | undefined {
  return game.id;
}

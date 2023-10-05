export interface IStateCode {
  id: number;
  stateCodeId?: string | null;
  code?: string | null;
  name?: string | null;
}

export type NewStateCode = Omit<IStateCode, 'id'> & { id: null };

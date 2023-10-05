export interface IProductUnit {
  id: number;
  unitCodeId?: string | null;
  unitCode?: string | null;
  unitCodeDescription?: string | null;
}

export type NewProductUnit = Omit<IProductUnit, 'id'> & { id: null };

// sort.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sort',
  pure: false
})
export class SortPipe implements PipeTransform {

  transform(
    items: any[],
    sortFields?: string | string[] | null, // Hacerlo opcional
    caseSensitive: boolean = false
  ): any[] {
    // Validación inicial: si no hay items
    if (!items || !Array.isArray(items) || items.length === 0) {
      return items || [];
    }

    // Si sortFields es null, undefined o vacío, devolver items sin ordenar
    if (!sortFields || (Array.isArray(sortFields) && sortFields.length === 0)) {
      return items;
    }

    // Convertir a array si es string
    const fields = Array.isArray(sortFields) ? sortFields : [sortFields];
    
    // Filtrar campos vacíos o inválidos
    const validFields = fields.filter(field => field && typeof field === 'string' && field.trim() !== '');
    
    if (validFields.length === 0) {
      return items;
    }

    // Crear una copia para no mutar el original
    const sorted = [...items];

    return sorted.sort((a, b) => {
      for (const field of validFields) {
        const isDescending = field.startsWith('-');
        const fieldName = isDescending ? field.slice(1) : field;
        
        const valueA = this.getNestedValue(a, fieldName);
        const valueB = this.getNestedValue(b, fieldName);

        const comparison = this.compareValues(valueA, valueB, caseSensitive);
        
        if (comparison !== 0) {
          return isDescending ? -comparison : comparison;
        }
      }
      return 0;
    });
  }

  private getNestedValue(obj: any, path: string): any {
    if (!path || !obj) return undefined;
    
    const parts = path.split('.');
    let current = obj;
    
    for (const part of parts) {
      if (current === null || current === undefined || typeof current !== 'object') {
        return undefined;
      }
      current = current[part];
    }
    
    return current;
  }

  private compareValues(a: any, b: any, caseSensitive: boolean = false): number {
    // Manejar null/undefined
    if (a === null || a === undefined) return 1;
    if (b === null || b === undefined) return -1;

    // Si son números
    if (typeof a === 'number' && typeof b === 'number') {
      return a - b;
    }

    // Si son booleanos
    if (typeof a === 'boolean' && typeof b === 'boolean') {
      return a === b ? 0 : (a ? 1 : -1);
    }

    // Si son fechas
    if (a instanceof Date && b instanceof Date) {
      return a.getTime() - b.getTime();
    }

    // Si son strings
    if (typeof a === 'string' && typeof b === 'string') {
      const strA = caseSensitive ? a : a.toLowerCase();
      const strB = caseSensitive ? b : b.toLowerCase();
      return strA.localeCompare(strB);
    }

    // Convertir a string para otros casos
    const strA = String(a);
    const strB = String(b);
    return caseSensitive ? strA.localeCompare(strB) : strA.toLowerCase().localeCompare(strB.toLowerCase());
  }
}
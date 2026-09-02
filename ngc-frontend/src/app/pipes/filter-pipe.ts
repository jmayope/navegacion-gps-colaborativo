// filter.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filter',
  pure: false // Cambiar a true si quieres mejor rendimiento y usas ChangeDetectionStrategy.OnPush
})
export class FilterPipe implements PipeTransform {

  /**
   * Filtra un array de objetos por múltiples criterios
   * @param items - Array a filtrar
   * @param searchTerm - Término de búsqueda
   * @param searchFields - Campos a buscar (si no se especifica, busca en todos)
   * @param exactMatch - Si debe ser coincidencia exacta (default: false)
   * @param caseSensitive - Si es sensible a mayúsculas (default: false)
   * @param excludeFields - Campos a excluir de la búsqueda
   * @returns Array filtrado
   */
  transform(
    items: any[],
    searchTerm: string | null | undefined | boolean | number,
    searchFields?: string[],
    exactMatch: boolean = false,
    caseSensitive: boolean = false,
    excludeFields?: string[]
  ): any[] {
    // Si no hay items o no es un array
    if (!items || !Array.isArray(items)) {
      return [];
    }

    // Convertir searchTerm a string seguro
    const searchStr = this.safeToString(searchTerm);
    
    // Si searchTerm está vacío después de la conversión, devolver todo
    if (!searchStr || searchStr.trim() === '') {
      return items;
    }

    const term = caseSensitive ? searchStr.trim() : searchStr.trim().toLowerCase();

    return items.filter(item => {
      return this.searchInItem(item, term, searchFields, exactMatch, caseSensitive, excludeFields);
    });
  }

  /**
   * Convierte cualquier valor a string de forma segura
   */
  private safeToString(value: any): string {
    if (value === null || value === undefined) {
      return '';
    }
    
    if (typeof value === 'boolean') {
      return value ? 'true' : 'false';
    }
    
    if (typeof value === 'number') {
      return String(value);
    }
    
    if (typeof value === 'string') {
      return value;
    }
    
    // Para otros tipos, convertir a string
    return String(value);
  }

  /**
   * Busca en un item individual
   */
  private searchInItem(
    item: any,
    searchTerm: string,
    searchFields?: string[],
    exactMatch: boolean = false,
    caseSensitive: boolean = false,
    excludeFields?: string[]
  ): boolean {
    // Si el item es null o undefined
    if (!item) return false;

    // Si es un tipo primitivo (string, number, boolean)
    if (typeof item !== 'object' || item === null) {
      return this.matchesValue(item, searchTerm, exactMatch, caseSensitive);
    }

    // Si es un array, buscar en cada elemento
    if (Array.isArray(item)) {
      return item.some(subItem => 
        this.searchInItem(subItem, searchTerm, searchFields, exactMatch, caseSensitive, excludeFields)
      );
    }

    // Si es un objeto, buscar en las propiedades
    const fieldsToSearch = searchFields || Object.keys(item).filter(key => 
      !excludeFields || !excludeFields.includes(key)
    );

    for (const field of fieldsToSearch) {
      const value = this.getNestedValue(item, field);
      
      // Si el valor es null o undefined, saltar
      if (value === null || value === undefined) continue;

      // Si el valor es un objeto o array, búsqueda recursiva
      if (typeof value === 'object' && !Array.isArray(value)) {
        if (this.searchInItem(value, searchTerm, undefined, exactMatch, caseSensitive)) {
          return true;
        }
        continue;
      }

      // Coincidencia directa
      if (this.matchesValue(value, searchTerm, exactMatch, caseSensitive)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Obtiene valor anidado de un objeto usando notación de puntos
   * Ej: 'user.profile.name' -> item.user.profile.name
   */
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

  /**
   * Verifica si un valor coincide con el término de búsqueda
   */
  private matchesValue(
    value: any,
    searchTerm: string,
    exactMatch: boolean = false,
    caseSensitive: boolean = false
  ): boolean {
    if (value === null || value === undefined) return false;

    // Convertir el valor a string de forma segura
    const strValue = this.safeToString(value);
    let strSearch = searchTerm;

    if (!caseSensitive) {
      return strValue.toLowerCase().includes(strSearch.toLowerCase());
    }

    if (exactMatch) {
      return strValue === strSearch;
    }

    return strValue.includes(strSearch);
  }
}
export class Arrays {
    static isEmpty(array: any[] | null | undefined): boolean {
        return !array || Array.isArray(array) && array.length === 0;
    }

    static isNotEmpty(array: any[] | null | undefined): boolean {
        return Array.isArray(array) && array.length > 0;
    }

    static size(array: any[] | null | undefined): number {
        return Array.isArray(array) ? array.length : 0;
    }
}
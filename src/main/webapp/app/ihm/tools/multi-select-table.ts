export default class MultiSelectTable<T> {
  readonly selectedElements = new Set<T>();

  constructor(private identifier: (t: T) => any = e => e,
    private active = true) {
  }

  onChangeActive(): void {
    this.active = !this.active;
    if (!this.active) {
      this.selectedElements.clear();
    }
  }

  select(element: T): void {
    if (!this.active) {
      return;
    }
    const id = this.identifier(element);
    if (this.selectedElements.has(id)) {
      this.selectedElements.delete(id);
    } else {
      this.selectedElements.add(id);
    }
  }

  isSelected(element: T): boolean {
    const id = this.identifier(element);
    return this.selectedElements.has(id);
  }

  isActive = (): boolean => this.active;

  isEmpty = (): boolean => this.selectedElements.size === 0;
}

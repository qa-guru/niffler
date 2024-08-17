interface Pageable<T> {
    content: T[],
    number: number,
    empty: boolean,
    first: boolean,
    last: boolean,
    numberOfElements: number,
}

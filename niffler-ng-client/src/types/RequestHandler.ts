export interface RequestHandler<T> {
    onSuccess: (data: T) => void,
    onFailure: (e: Error) => void,
}

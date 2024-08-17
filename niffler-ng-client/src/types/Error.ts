export class ApiError extends Error {
    public readonly detail: string;
    public readonly status: number;
    public readonly name = "ApiError";

    constructor(detail: string, status: number) {
        super(detail);
        this.detail = detail;
        this.status = status;
    }
}

interface CommonError extends Error {
    message: string,
    name: "CommonError",
}

export function isCommonError(error: any): error is CommonError {
    return "message" in error;
}

export function isApiError(error: any): error is ApiError {
    return "detail" in error;
}
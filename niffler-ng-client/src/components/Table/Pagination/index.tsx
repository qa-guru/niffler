import {Box} from "@mui/material"
import LoadingButton from '@mui/lab/LoadingButton';
import {FC} from "react"

interface TablePaginationInterface {
    hasNextValues: boolean;
    hasPreviousValues: boolean;
    onPreviousClick: () => void;
    onNextClick: () => void;
    isNextButtonLoading: boolean;
    isPreviousButtonLoading: boolean;
}

export const TablePagination: FC<TablePaginationInterface> = ({
                                                                  hasNextValues,
                                                                  hasPreviousValues,
                                                                  onPreviousClick,
                                                                  onNextClick,
                                                                  isPreviousButtonLoading,
                                                                  isNextButtonLoading
                                                              }) => {
    return (
        <Box sx={{
            display: "flex",
            width: "100%",
            alignItems: "center",
            justifyContent: "flex-end",
        }}>
            <LoadingButton
                type="button"
                id="page-prev"
                sx={{
                    margin: "4px",
                    width: 100,
                }}
                variant="outlined"
                disabled={!hasPreviousValues}
                onClick={onPreviousClick}
                loading={isPreviousButtonLoading}

            >
                Previous
            </LoadingButton>
            <LoadingButton
                type="button"
                id="page-next"
                sx={{
                    margin: "4px",
                    width: 100,
                    minHeight: "50px",
                }}
                disabled={!hasNextValues}
                loading={isNextButtonLoading}
                variant="outlined"
                onClick={onNextClick}

            >
                Next
            </LoadingButton>
        </Box>
    )
}

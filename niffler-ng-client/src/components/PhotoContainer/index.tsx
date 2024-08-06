import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import { FC } from "react";
import { PhotoCard } from "../PhotoCard";
import { Photo } from "../../types/Photo";

interface PhotoContainerInterface {
    data: Photo[];
    onSelectImage: (photo: Photo) => void;
}
export const PhotoContainer: FC<PhotoContainerInterface> = ({onSelectImage, data}) => {

    return (
        <Container>
            <Grid container spacing={3}>
                {data.map((item: Photo) => (
                    <Grid item key={item.id} xs={3}>
                    <PhotoCard
                        photo={item}
                        onEditClick={onSelectImage}
                    />
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};
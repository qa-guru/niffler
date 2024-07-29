import {Box, Button, IconButton, Typography, useTheme } from '@mui/material';
import Paper from '@mui/material/Paper';
import FavoriteOutlinedIcon from '@mui/icons-material/FavoriteOutlined';
import { FC, useContext } from 'react';
import "./styles.css";
import { Photo } from '../../types/Photo';
import FavoriteBorderOutlinedIcon from '@mui/icons-material/FavoriteBorderOutlined';
import { SessionContext } from '../../context/SessionContext';
import { useSnackBar } from '../../context/SnackBarContext';

interface PhotoCardInterface {
    photo: Photo;
    onEditClick: (photo: Photo) => void;
}
export const PhotoCard: FC<PhotoCardInterface> = ({photo, onEditClick}) => {
    const {user} = useContext(SessionContext);
    const snackbar = useSnackBar();
    const theme = useTheme();


    const handleDeletePhoto = () => {

    }

    const handleLikePhoto = () => {

    }

    return (
            <Paper elevation={3}>
                <img
                    className="photo-card__image"
                    src={photo.src}
                    alt={photo.description}
                />
                <Box paddingX={1.25}>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                        }}
                    >
                        <FavoriteOutlinedIcon sx={{width: 15}}/>
                        <Typography component="p" variant="body2" marginLeft={0.5}>
                            {photo.likes.total} likes
                        </Typography>
                        <IconButton
                            aria-label="like" size="small"
                            sx={{
                                marginLeft: "auto",
                                color: theme.palette.primary.main,
                            }}
                            onClick={handleLikePhoto}
                        >
                            {
                                photo.likes?.likes.some((el) => el.user === user?.id!!) ?
                                    <FavoriteOutlinedIcon/> :
                                    <FavoriteBorderOutlinedIcon/>
                            }
                        </IconButton>
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                        }}
                    >
                        <Typography component="h3" variant="subtitle1">
                            <img width={20} src={photo.country.flag} alt={photo.country.name}/> {photo.country.name}
                        </Typography>
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                        }}
                    >
                        <Typography component="p" variant="body2" className="photo-card__content" color="secondary">
                            {photo.description}
                        </Typography>
                    </Box>
                    <Box
                        sx={{
                            display: "flex",
                            alignItems: "center",
                            justifyContent: "space-between",
                        }}
                    >
                        <Button variant="contained" sx={{margin: 2}} onClick={() => onEditClick(photo)}>Edit</Button>
                        <Button sx={{margin: 2}} onClick={handleDeletePhoto}>Delete</Button>
                    </Box>
                </Box>
            </Paper>
    );
};

import {createTheme} from '@mui/material/styles';

const {palette} = createTheme({});
const theme = createTheme({
    palette: {
        primary: {
            main: "#2941CC",
            contrastText: "#FFFFFF",
        },
        success: {
            main: "#35AD7B",
            contrastText: "#FFFFFF"
        },
        secondary: {
            main: "#F5F6FA",
            light: "#FAFAFD",
        },
        error: {
            main: "#d32f2f",
        },
        info: {
            main: "#A8ACC0",
        },
        blue100: palette.augmentColor({
            color: {
                main: "#2941CC",
            },
        }),
        blue200: palette.augmentColor({
            color: {
                main: "#162995",
            },
        }),
        blue10: palette.augmentColor({
            color: {
                main: "#EAECFA",
            },
        }),
        purple: palette.augmentColor({
            color: {
                main: "#9455C6",
            },
        }),
        azure: palette.augmentColor({
            color: {
                main: "#219EBC",
            },
        }),
        skyBlue: palette.augmentColor({
            color: {
                main: "#63B5E2",
            },
        }),
        green: palette.augmentColor({
            color: {
                main: "#35AD7B",
            },
        }),
        yellow: palette.augmentColor({
            color: {
                main: "#FFB703",
            },
        }),
        orange: palette.augmentColor({
            color: {
                main: "#FB8500",
            },
        }),
        red: palette.augmentColor({
            color: {
                main: "#F75943",
            },
        }),
        black: palette.augmentColor({
            color: {
                main: "#242527",
            }
        }),
        gray_600: palette.augmentColor({
            color: {
                main: "#A8ACC0",
            }
        }),
    },
    breakpoints: {
        values: {
            xs: 0,
            sm: 650,
            md: 1010,
            lg: 1025,
            xl: 1536,
        }
    },
    typography: {
        fontFamily: ['Inter', 'sans-serif'].join(','),
        h1: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
        h2: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
        h3: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
        h4: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
        h5: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
        h6: {
            fontFamily: ['Young serif', 'serif'].join(','),
        },
    },
    components: {
        MuiTextField: {
            styleOverrides: {
                root: {
                    borderRadius: "8px",
                }
            }
        },
        MuiOutlinedInput: {
            styleOverrides: {
                input: {
                    padding: "13px 16px",
                    backgroundColor: "#FAFAFD",
                },
                notchedOutline: {
                    borderRadius: "8px",
                    border: "1px solid #E4E6F1",
                }
            }
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    padding: "12px 16px",
                    fontWeight: "600",
                    textTransform: "none",
                    borderRadius: "8px"
                }
            }
        },
        MuiIconButton: {
            styleOverrides: {
                colorInfo: {
                    backgroundColor: "#2941CC",
                }
            }
        },
        MuiChip: {
            styleOverrides: {
                root: {
                    backgroundColor: "#EAECFA",
                    borderRadius: "20px",
                    fontSize: "16px",
                    fontWeight: "400",
                    pagging: "8px 16px"
                },
                colorPrimary: {
                    backgroundColor: "#2941CC",
                },
            }
        },
        MuiAvatar: {
            styleOverrides: {
                root: {
                    backgroundColor: "#A8ACC0",

                }
            }
        },
        MuiToolbar: {
            styleOverrides: {
                root: {
                    padding: "2px",
                    '@media (min-width: 600px)': {
                        padding: "2px"
                    }
                },

            }
        },
        MuiTableCell: {
            styleOverrides: {
                root: {
                    border: "none",
                    padding: "10px",
                },
            }
        },
        MuiTabs: {
            styleOverrides: {
                indicator: {
                    backgroundColor: "#242527",
                }
            }
        },
        MuiTab: {
            styleOverrides: {
                root: {
                    borderBottom: "1px solid #E4E6F1",
                }
            }
        },
        MuiDrawer: {
            styleOverrides: {
                paper: {
                    width: "100%",
                }
            }
        }
    }
});

declare module '@mui/material/styles' {
    interface Palette {
        blue100: Palette['primary'];
        blue200: Palette['primary'];
        blue10: Palette['primary'];
        purple: Palette['primary'];
        azure: Palette['primary'];
        skyBlue: Palette['primary'];
        green: Palette['primary'];
        yellow: Palette['primary'];
        orange: Palette['primary'];
        red: Palette['primary'];
        black: Palette['primary'];
        gray_600: Palette['primary'];
    }

    interface PaletteOptions {
        blue100: PaletteOptions['primary'];
        blue200: PaletteOptions['primary'];
        blue10: PaletteOptions['primary'];
        purple: PaletteOptions['primary'];
        azure: PaletteOptions['primary'];
        skyBlue: PaletteOptions['primary'];
        green: PaletteOptions['primary'];
        yellow: PaletteOptions['primary'];
        orange: PaletteOptions['primary'];
        red: PaletteOptions['primary'];
        black: PaletteOptions['primary'];
        gray_600: PaletteOptions['primary'];
    }
}

declare module '@mui/material/Button' {
    interface ButtonPropsColorOverrides {
        orange: true,
    }
}

export default theme;
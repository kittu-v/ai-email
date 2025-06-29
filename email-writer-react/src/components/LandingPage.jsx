import { Box, Button, Container, Typography } from '@mui/material'
import './App.css'
import { useNavigate } from 'react-router-dom'

export default function LandingPage(){
    const navigate=useNavigate();

    return(
        <Container maxWidth="md">
            <Typography align='center' variant='h2' component='h1' gutterBottom color='warning.dark'
            sx={{textAlign:'center', mx: 8, py:8}}  >Smart Email Assistant 

            </Typography>
            <Box sx={{mx:5}} justifyContent={'center'} display={'flex'}>
                <Button variant='contained' color='secondary' sx={{mr:5}}
                onClick={() =>
                    navigate("/reply")
                }>
                    AI Email Reply
                </Button>

                <Button variant='contained' color='secondary'
                onClick={() =>
                    navigate("/generate")
                }>
                    AI Email Generate
                </Button>

            </Box>
        </Container>
    )
}
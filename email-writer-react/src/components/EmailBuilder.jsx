
import { useState } from 'react'
import './App.css'
import { Box, Button, CircularProgress, Container, Fab, FormControl, InputLabel, MenuItem, Select, TextField, Typography } from '@mui/material';
import axios from 'axios';
//import ContentCopyIcon from '@mui/icons-material/ContentCopy';


function App() {
   const [emailBuildRequest, setEmailBuildRequest]=useState('');
    const [tone, setTone]=useState('');
    const [length, setLength]=useState('');
    const [recipientName, setRecipientName]=useState('');
    const [senderName, setSenderName]=useState('');
    const [generatedEmail, setGeneratedEmail]=useState('');
    const [loading, setLoading]=useState(false);
    const [error, setError]=useState('');

    const handleSubmit=async () =>{
      setLoading(true);
      setError('');

      try {
        const response = await axios.post("http://localhost:8080/api/email/build",{
          emailBuildRequest,
          tone,
          length,
          recipientName,
          senderName
        });
        setGeneratedEmail (typeof response.data === "string" ? response.data : JSON.stringify(response.data));
      } catch (error) {
        setError("Failed to Generate Email. Please Try Again")
        console.error(error);
      }
      finally{
        setLoading(false);
      }
      
    }
  return (
   
    <Container maxWidth='md' sx={{py:4}}>
      <Typography variant='h3' component="h1" sx={{mx: 4}} gutterBottom>
        Smart Email Generator
      </Typography>

      <Box sx={{mx:3}}>
        <TextField 
        fullWidth
        multiline
        rows={6}
        variant='outlined'
        label="Original Email Content"
        value={emailBuildRequest || ''}
        onChange={(e) => setEmailBuildRequest(e.target.value)}
        sx={{mb: 2}}
        />

        <FormControl fullWidth sx={{mb : 2}}>
          <InputLabel>Tone (Optional) </InputLabel>
          <Select 
          value={tone || ''}
          label={"Tone (Optional)"}
          onChange={(e) => setTone(e.target.value)}>
            <MenuItem value="">None</MenuItem>
            <MenuItem value="Professional">Professional</MenuItem>
            <MenuItem value="Casual">Casual</MenuItem>
            <MenuItem value="Friendly">Friendly</MenuItem>
          </Select>

        </FormControl>

        <FormControl fullWidth sx={{mb : 2}}>
          <InputLabel>Choose Length (Optional) </InputLabel>
          <Select 
          value={length || ''}
          label={"CHoose Length (Optional)"}
          onChange={(e) => setLength(e.target.value)}>
            <MenuItem value="">None</MenuItem>
            <MenuItem value="Short">Short</MenuItem>
            <MenuItem value="Medium">Medium</MenuItem>
            <MenuItem value="Large">Large</MenuItem>
          </Select>
        </FormControl>
        
        <FormControl fullWidth sx={{mb:2}}>
          
          <TextField 
        fullWidth
        multiline
        rows={1}
        variant='outlined'
        label="Recipient Name (Optional) "
        value={recipientName || ''}
        onChange={(e) => setRecipientName(e.target.value)}
        
        />
        </FormControl>

        <FormControl fullWidth sx={{mb:2}}>
          
        <TextField 
        fullWidth
        multiline
        rows={1}
        variant='outlined'
        label="Sender Name (Optional) "
        value={senderName || ''}
        onChange={(e) => setSenderName(e.target.value)}
        sx={{mb: 2}}
        />
        </FormControl>

        <Button variant='contained'
        onClick={handleSubmit}
        disabled={!emailBuildRequest||loading}
        fullWidth>
          {loading ? <CircularProgress size={24}/> : "Generate Email"}
        </Button>

      </Box>

      {error && (
        <Typography color='error' sx={{mb:2}}>
          {error}
        </Typography>
      )}

      {generatedEmail && (
        <Box sx={{mx: 3, mt: 3}}>
          <Typography variant='h6' gutterBottom>
            Generated Email:
          </Typography>

          <TextField
          fullWidth
          multiline
          rows={6}
          variant='outlined'
          value={generatedEmail || ''}
          inputProps={{readOnly:true}}
          />

        <Button variant='outlined'
        sx={{mt:3}}
        onClick={() => navigator.clipboard.writeText(generatedEmail)}>
          Copy to Clipboard
        </Button>
        </Box>
      )}
     
    </Container>

  )
}
export default App
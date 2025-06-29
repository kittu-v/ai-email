// App.jsx
import { Routes, Route } from 'react-router-dom'
import LandingPage from './components/LandingPage.jsx'
import EmailReply from './components/EmailReply.jsx'
import EmailBuilder from './components/EmailBuilder.jsx'

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/reply" element={<EmailReply />} />
      <Route path="/generate" element={<EmailBuilder />} />
    </Routes>
  )
}

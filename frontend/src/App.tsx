import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import SearchPage from './pages/SearchPage'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<SearchPage />} />
      {/* later: <Route path="/details/:offerId" element={<DetailsPage />} /> */}
    </Routes>
  );
}


export default App

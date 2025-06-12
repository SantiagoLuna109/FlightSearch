import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import SearchPage from './pages/SearchPage'
import ResultsPage from './pages/ResultsPage'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<SearchPage />} />
      <Route path="/results" element={<ResultsPage/>}/>
      {/* New */}
    </Routes>
  );
}


export default App

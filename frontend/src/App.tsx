import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import SearchPage from './pages/SearchPage'
import ResultsPage from './pages/ResultsPage'
import DetailsPage from './pages/DetailsPage'
import './App.css'

function App() {
  return (
    <Routes>
      <Route path="/" element={<SearchPage />} />
      <Route path="/results" element={<ResultsPage/>}/>
      <Route path="/details/:id" element={<DetailsPage/>}/>
    </Routes>
  );
}


export default App

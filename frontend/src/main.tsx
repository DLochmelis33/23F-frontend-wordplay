import React from 'react'
import ReactDOM from 'react-dom/client'
import { GameComponent } from './App.tsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <GameComponent />
  </React.StrictMode>,
)

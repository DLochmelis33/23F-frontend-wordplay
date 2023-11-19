import React, { useState } from 'react'
import './App.css'

export const GameComponent: React.FC = () => {
  console.log("ya")
  const [userInput, setUserInput] = useState("uu")
  const [gameLine, setGameLine] = useState("gl")

  return (<>
    <span id='currentLine'>{gameLine}</span>
    <input
      id='userInput'
      value={userInput}
      onChange={e => {
        const newValue = e.target.value
        const filtered = newValue.toLowerCase().replace(/[^a-z]/g, '') // drop all non-alphabetic characters
        setUserInput(filtered)
      }}
      onKeyDown={e => {
        if (e.key == 'Enter') {
          setGameLine(gameLine + userInput)
          setUserInput('')
        }
      }} />
  </>);
}

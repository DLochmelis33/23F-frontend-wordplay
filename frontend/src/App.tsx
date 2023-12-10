import React, { useEffect, useState } from 'react'
import './App.css'
import useWebSocket from 'react-use-websocket'

export const GameCoreUI: React.FC = () => {
  const [userInput, setUserInput] = useState("")
  const [gameLine, setGameLine] = useState("banana")

  return (<>
    <span id='currentLine'>{gameLine}
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
    </span>
  </>);
}

type UserMessage = UserMove

type UserMove = {
  userSuffix: string
}

export const GameMainComponent: React.FC = () => {
  const { sendMessage, lastMessage } = useWebSocket('ws://127.0.0.1:80/test')

  useEffect(() => {
    const msg: UserMessage = {
      userSuffix: 'haha'
    }
    sendMessage(JSON.stringify(msg))
  }, [sendMessage])

  useEffect(() => {
    console.log(lastMessage?.data)
  }, [lastMessage])

  return GameCoreUI({})
}

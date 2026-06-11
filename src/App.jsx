import { useState } from 'react'

function App() {
  const [user, setUser] = useState('')
  const [password, setPassword] = useState('')

  const lidarComLogin = async (e) => {
    e.preventDefault()

    const dadosParaOBackend = {
      user: user,
      password: password
    }

    try {
      const resposta = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(dadosParaOBackend), 
      })

      const dadosDoServidor = await resposta.json()

      if (resposta.ok) {
        alert('Boa! O Spring aceitou o login de verdade!')
        console.log('Dados recebidos do Java (Token/User):', dadosDoServidor)
      } else {
        alert('Erro no login: Usuário ou senha incorretos.')
      }

    } catch (error) {
      alert('Erro ao conectar! O servidor Spring está ligado e com @CrossOrigin ativado?')
      console.error('Detalhes do erro:', error)
    }
  }

  return (
    <div style={{ 
      display: 'flex', justifyContent: 'center', alignItems: 'center', 
      height: '100vh', fontFamily: 'sans-serif', backgroundColor: '#f0f2f5'
    }}>
      <form onSubmit={lidarComLogin} style={{
        backgroundColor: '#fff', padding: '40px', borderRadius: '8px',
        boxShadow: '0 4px 12px rgba(0,0,0,0.1)', width: '320px',
        display: 'flex', flexDirection: 'column', gap: '16px'
      }}>
        <h2 style={{ textAlign: 'center', margin: '0 0 8px 0', color: '#333' }}>GoGraduation</h2>
        
        <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
          <label style={{ fontSize: '14px', color: '#997777' }}>Usuário</label>
          <input 
            type="text" 
            value={user}
            onChange={(e) => setUser(e.target.value)} 
            required 
            style={{ padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '6px' }}>
          <label style={{ fontSize: '14px', color: '997777' }}>Senha</label>
          <input 
            type="password" 
            value={password}
            onChange={(e) => setPassword(e.target.value)} 
            required 
            style={{ padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }}
          />
        </div>

        <button type="submit" style={{
          padding: '12px', borderRadius: '4px', border: 'none',
          backgroundColor: '#007bff', color: '#fff', fontWeight: 'bold',
          cursor: 'pointer', marginTop: '10px'
        }}>
          Entrar
        </button>
      </form>
    </div>
  )
}

export default App
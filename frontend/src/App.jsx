import { useState } from 'react'
import './App.css'

function App() {
  const [url, setUrl] = useState('')
  const [scanResults, setScanResults] = useState(null)
  const [scanStatus, setScanStatus] = useState('idle')
  async function handleScan() {
  setScanStatus('scanning')
  setScanResults(null)

  try {
    const response = await fetch('http://localhost:8080/api/scans', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ url }),
    })

    const data = await response.json()

    if (!response.ok || data.status !== 'completed') {
      throw new Error(data.error || 'Scan failed')
    }

    setScanResults(data.results)
    setScanStatus('completed')

    console.log('Scan response:', data)
  } catch (error) {
    console.error('Scan request failed:', error)
    setScanStatus('failed')
  }
}
  return (
    <div className="app">
      <header className="header">
        <a className="logo" href="/">
          <span className="logo-icon">D</span>
          <span>DevAccess</span>
        </a>

        <nav className="navigation">
          <a href="#scanner" className="active">Scanner</a>
          <a href="#history">History</a>
          <a href="#about">About</a>
        </nav>
      </header>

      <main className="main-content">
        <section className="intro">
          <span className="eyebrow">WEB ACCESSIBILITY ANALYSIS</span>
          <h1>Make your website more accessible.</h1>
          <p>
            Scan your website for accessibility issues and get
            actionable insights to improve the experience for everyone.
          </p>
        </section>

        <section className="scanner-card" id="scanner">
          <label htmlFor="website-url">Website URL</label>

          <div className="scan-form">
            <input
              id="website-url"
              type="url"
              placeholder="https://example.com"
              value={url}
              onChange={(event) => setUrl(event.target.value)}
            />
            <button type="button" onClick={handleScan}>
              Scan website
            </button>
          </div>

          <p className="helper-text">
            Enter a publicly accessible website URL to begin.
          </p>
        </section>

        <section className="results-section">
          <div className="section-heading">
            <h2>Accessibility overview</h2>
            <span>No scan performed yet</span>
          </div>

          <div className="results-grid">
            <article className="result-card">
              <span className="result-label">Passed</span>
              <strong>—</strong>
              <span className="result-description">Checks passed</span>
            </article>

            <article className="result-card">
              <span className="result-label">Violations</span>
              <strong>{scanResults ? scanResults.violations.length : '-'}</strong>
              <span className="result-description">Issues detected</span>
            </article>

            <article className="result-card">
              <span className="result-label">Incomplete</span>
              <strong>{scanResults ? scanResults.incomplete.length : '-'}</strong>
              <span className="result-description">Need manual review</span>
            </article>
          </div>
        </section>
        {scanResults && (
          <section className="findings-section">
            <div className="section-heading">
              <h2>Accessibility findings</h2>
              <span>
                {scanResults.violations.length} violations found
              </span>
            </div>

            <div className="findings-list">
              {scanResults.violations.map((finding) => (
                <article className="finding-card" key={finding.ruleId}>
                  <div className="finding-header">
                    <h3>{finding.help}</h3>
                    <span className={`severity ${finding.impact}`}>
                      {finding.impact}
                    </span>
                  </div>

                  <p>{finding.description}</p>
                  <span className='affected-count'>
                    {finding.nodes.length} affected {finding.nodes.length === 1 ? 'element' : 'elements'}
                  </span>
                  <details className='finding-details'>
                    <summary>View affected elements</summary>

                    {finding.nodes.map((node, index) => (
                      <div className='node-details' key={`${finding.ruleId}-${index}`}>
                        <p><strong>CSS selector:</strong> {node.target.join(', ')}</p>

                        <p><strong>HTML:</strong></p>
                        <pre>{node.html}</pre>

                        <p><strong>Why it failed:</strong></p>
                        <pre>{node.failureSummary}</pre>
                      </div>
                    ))}
                  </details>
                </article>
              ))}
          </div>
        </section>
      )}
      {scanResults && scanResults.incomplete.length > 0 && (
        <section className="findings-section">
          <div className="section-heading">
            <h2>Needs manual review</h2>
            <span>{scanResults.incomplete.length} checks to review</span>
          </div>

          <div className="findings-list">
            {scanResults.incomplete.map((finding) => (
              <article className="finding-card" key={finding.ruleId}>
                <div className="finding-header">
                  <h3>{finding.help}</h3>
                  <span className="severity incomplete">Review</span>
                </div>

                <p>{finding.description}</p>

                <span className="affected-count">
                  {finding.nodes.length} affected {finding.nodes.length === 1 ? 'element' : 'elements'}
                </span>

                <details className="finding-details">
                  <summary>View affected elements</summary>

                  {finding.nodes.map((node, index) => (
                    <div className="node-details" key={`${finding.ruleId}-${index}`}>
                      <p><strong>CSS selector:</strong> {node.target.join(', ')}</p>

                      <p><strong>HTML:</strong></p>
                      <pre>{node.html}</pre>

                      <p><strong>Why it needs review:</strong></p>
                      <pre>{node.failureSummary}</pre>
                    </div>
                  ))}
                </details>
              </article>
            ))}
          </div>
        </section>
      )}
      </main>

      <footer className="footer">
        DevAccess · Website accessibility analysis
      </footer>
    </div>
  )
}

export default App

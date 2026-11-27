import { useState } from 'react'
import './App.css'

function App() {
  const [url, setUrl] = useState('')
  const [currentView, setCurrentView] = useState('scanner')
  const [scanHistory, setScanHistory] = useState([])
  const [historyStatus, setHistoryStatus] = useState('idle')
  const [scanResults, setScanResults] = useState(null)
  const [scannedUrl, setScannedUrl] = useState('')
  const [scanStatus, setScanStatus] = useState('idle')
  const [errorMessage, setErrorMessage] = useState('')
  const [severityFilter, setSeverityFilter] = useState('all')
  const totalAffectedElements = scanResults
    ? scanResults.violations.reduce(
      (total, finding) => total + finding.nodes.length,
      0
      )
    : 0
  const filteredViolations = scanResults
    ? severityFilter === 'all'
      ? scanResults.violations
      : scanResults.violations.filter(
        (finding) => finding.impact === severityFilter
        )
    : []

  async function loadScanHistory() {
    setHistoryStatus('loading')

    try {
      const response = await fetch('http://localhost:8080/api/scans')

      if (!response.ok) {
        throw new Error('Unable to load scan history.')
      }

      const data = await response.json()

      setScanHistory(data)
      setHistoryStatus('loaded')
    } catch (error) {
      console.error('History request failed:', error)
      setHistoryStatus('failed')
    }
  }

  async function loadScanDetails(scanId) {
  try {
    const response = await fetch(`http://localhost:8080/api/scans/${scanId}`)

    if (!response.ok) {
      throw new Error('Unable to load scan details.')
    }

    const data = await response.json()

    setScanResults(data.scanResults.results)
    setScannedUrl(data.url)
    setSeverityFilter('all')
    setCurrentView('scanner')
  } catch (error) {
    console.error('Scan details request failed:', error)
  }
}

  async function handleScan(urlToScan = url) {
    if (!urlToScan.trim() || scanStatus === 'scanning') {
      return
    }

    setScanStatus('scanning')
    setErrorMessage('')
    setScanResults(null)

  try {
    const response = await fetch('http://localhost:8080/api/scans', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ url: urlToScan }),
    })

    const data = await response.json()

    if (!response.ok || data.status !== 'completed') {
      throw new Error(data.error || 'Scan failed')
    }

    setScanResults(data.results)
    setScannedUrl(urlToScan)
    setScanStatus('completed')

    console.log('Scan response:', data)
  } catch (error) {
    console.error('Scan request failed:', error)
    setErrorMessage(
      error instanceof TypeError
        ? 'Unable to connect to DevAccess. Please check that the backend is running and try again.'
        : error.message || 'Something went wrong. Please try again.'
      )
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
          <button
            type='button'
            className={currentView === 'scanner' ? 'active' : ''}
            onClick={() => setCurrentView('scanner')}
          >
            Scanner
          </button>

          <button
            type='button'
            className={currentView === 'history' ? 'active' : ''}
            onClick={() => {
              setCurrentView('history')
              loadScanHistory()
            }}
          >
            History
          </button>
          <a href="#about">About</a>
        </nav>
      </header>

      <main className="main-content">
        {currentView === 'scanner' && (
          <>
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
            <button
              type="button"
              onClick={() => handleScan()}
              disabled={!url.trim() || scanStatus === 'scanning'}
            >
              {scanStatus === 'scanning' ? (
                <>
                  <span className='spinner'></span>
                  Scanning...
                </>
              ) : (
                'Scan website')}
            </button>
          </div>

          <p className="helper-text">
            Enter a publicly accessible website URL to begin.
          </p>
          {errorMessage && (
            <p className='error-message' role='alert'>
              {errorMessage}
            </p>
          )}
        </section>

        <section className="results-section">
          <div className="section-heading">
            <h2>Accessibility overview</h2>
            <div className='overview-actions'>
              <span>
                {scanResults ? `Results for ${scannedUrl}` : 'No scan performed yet'}
              </span>

              {scanResults && (
                <button
                  type='button'
                  onClick={() => handleScan(scannedUrl)}
                  disabled={scanStatus === 'scanning'}
                >
                  {scanStatus === 'scanning' ? 'Scanning...' : 'Rescan'}
                </button>
              )}
            </div>
          </div>

          <div className="results-grid">
            <article className="result-card">
              <span className="result-label">Passed</span>
              <strong>{scanResults ? scanResults.passes.length : '-'}</strong>
              <span className="result-description">Rules passed</span>
            </article>

            <article className="result-card">
              <span className="result-label">Violations</span>
              <strong>{scanResults ? scanResults.violations.length : '-'}</strong>
              <span className="result-description">Rules with issues</span>
            </article>

            <article className="result-card">
              <span className="result-label">Incomplete</span>
              <strong>{scanResults ? scanResults.incomplete.length : '-'}</strong>
              <span className="result-description">Rules needing review</span>
            </article>

            <article className="result-card">
              <span className="result-label">Affected elements</span>
              <strong>{scanResults ? totalAffectedElements : '-'}</strong>
              <span className="result-description">Elements with violations</span>
            </article>
          </div>
        </section>
        {scanResults && (
          <section className="findings-section">
            <div className="section-heading">
              <h2>Accessibility findings</h2>
              <span>
                {filteredViolations.length} of {scanResults.violations.length} violations shown
              </span>
            </div>
            <div className='severity-filters'>
              {['all', 'critical', 'serious', 'moderate', 'minor'].map((severity) => (
                <button
                  key={severity}
                  type='button'
                  className={severityFilter === severity ? 'filter-button active' : 'filter-button'}
                  onClick={() => setSeverityFilter(severity)}
                >
                  {severity === 'all' ? 'All' : severity.charAt(0).toUpperCase() + severity.slice(1)}
                </button>
              ))}
            </div>

            <div className="findings-list">
              {filteredViolations.length === 0 ? (
                <p className='empty-state'>
                  No violations found for this severity.
                </p>
              ) : (
                filteredViolations.map((finding) => (
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
                ))
            )}
          </div>
        </section>
      )}

      {scanResults && scanResults.passes.length > 0 && (
        <section className="findings-section">
          <div className="section-heading">
            <h2>Passed checks</h2>
            <span>{scanResults.passes.length} rules passed</span>
          </div>

          <div className="findings-list">
            {scanResults.passes.map((finding) => (
              <article className="finding-card" key={finding.ruleId}>
                <div className="finding-header">
                  <h3>{finding.help}</h3>
                  <span className="severity passed">Passed</span>
                </div>

                <p>{finding.description}</p>

                <details className="finding-details">
                  <summary>Learn more about this rule</summary>

                  <p>
                    <a href={finding.helpUrl} target="_blank" rel="noopener noreferrer">
                      View axe-core rule documentation
                    </a>
                  </p>
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
      </>
        )}

                {currentView === 'history' && (
                  <section className="results-section">
                    <div className="section-heading">
                      <h2>Scan history</h2>
                      <span>Your previously completed scans</span>
                    </div>

                    {historyStatus === 'loading' && (
                      <p>Loading scan history...</p>
                    )}

                    {historyStatus === 'failed' && (
                      <p className="error-message">
                        Unable to load scan history. Please try again.
                      </p>
                    )}

                    {historyStatus === 'loaded' && scanHistory.length === 0 && (
                      <p className="empty-state">
                        No previous scans found. Scan a website to get started.
                    </p>
                    )}

                    {historyStatus === 'loaded' && scanHistory.length > 0 && (
                      <div className="findings-list">
                        {scanHistory.map((scan) => (
                          <article
                            className="finding-card history-card"
                            key={scan.id}
                          >
                            <h3>{scan.url}</h3>
                            <p>
                              Scanned: {new Date(scan.scannedAt).toLocaleString()}
                            </p>
                            <button
                              type='button'
                              className='history-button'
                              onClick={() => loadScanDetails(scan.id)}
                              >
                                View scan results
                            </button>
                          </article>
                        ))}
                      </div>
                      )}
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

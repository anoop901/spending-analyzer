import React, { Component } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.css'

class App extends Component {

  constructor() {
    super();
    this.state = {
      loaded: false,
      entries: []
    };
  }

  componentDidMount() {
    window.fetch(new Request('api/entries')).then((response) => {
      return response.json();
    }).then((responseJson) => {
      this.setState({
        loaded: true,
        entries: responseJson.entries
      });
    });
  }

  render() {
    return (
      <div className="App container">
        <div className="App-header">
          <h1>Spending Analyzer</h1>
        </div>
        <button className="btn btn-default">Add</button>
        <button className="btn btn-default">Edit</button>
        <button className="btn btn-default">Delete</button>
        <button className="btn btn-default">Import</button>
        <EntryListComponent entries={this.state.entries} loaded={this.state.loaded} />
      </div>
    );
  }
}

class EntryListComponent extends Component {
  render() {
    return (
      <div>
        <table className="table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Description</th>
              <th>Amount</th>
              <th>Category</th>
            </tr>
          </thead>
          <tbody>
            {this.props.entries.map((entry, index) =>
              <EntryComponent entry={entry} key={entry.id} />
            )}
          </tbody>
        </table>
      </div>
    )
  }
}

class EntryComponent extends Component {
  render() {
    return (
      <tr>
        <td>{this.props.entry.date}</td>
        <td>{this.props.entry.description}</td>
        <td>{this.props.entry.amount}</td>
        <td>{this.props.entry.category}</td>
      </tr>
    )
  }
}

export default App;

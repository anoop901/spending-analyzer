import React, { Component } from 'react';
import './App.css';

import 'bootstrap/dist/css/bootstrap.css';
//import 'bootstrap/dist/css/bootstrap-theme.css';
import {Button, ButtonToolbar, Modal, Form, FormGroup, FormControl, ControlLabel} from 'react-bootstrap'

class App extends Component {

  constructor() {
    super();
    this.state = {
      loaded: false,
      showImportModal: true,
      importModalDataFormat: "",
      importModalFile: [],
      entries: []
    };
  }

  componentDidMount() {
    fetch('api/entries').then((response) => {
      return response.json();
    }).then((responseJson) => {
      this.setState({
        loaded: true,
        entries: responseJson.entries
      });
    });
  }

  openImportModal() {
    this.setState({ showImportModal: true });
  }

  closeImportModal() {
    this.setState({ showImportModal: false });
  }

  handleImportDataFormatChange(e) {
    this.setState({importModalDataFormat: e.target.value});
  }

  handleImportFileChange(e) {
    this.setState({importModalFile: e.target.files[0]});
  }

  handleImport() {

    const reader = new FileReader();
    reader.onload = ((e) => {
      const form = new FormData();
      form.append('dataFormat', this.state.importModalDataFormat);
      form.append('file', e.currentTarget.result);
      fetch('/api/import', {
        method: 'POST',
        body: form
      });
      this.closeImportModal();
    });
    reader.readAsText(this.state.importModalFile);
/*
    */
  }

  render() {
    return (
      <div className="container">
        <div>
          <h1>Spending Analyzer</h1>
        </div>
        <ButtonToolbar>
          <Button bsStyle="default">Add</Button>
          <Button bsStyle="default">Edit</Button>
          <Button bsStyle="default">Delete</Button>
          <Button
            bsStyle="primary"
            onClick={this.openImportModal.bind(this)}>Import</Button>
        </ButtonToolbar>
        <EntryListComponent entries={this.state.entries} loaded={this.state.loaded} />

        <Modal show={this.state.showImportModal} onHide={this.closeImportModal.bind(this)}>
          <Modal.Header closeButton>
            <Modal.Title>Import</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <FormGroup>
                <ControlLabel>Data format</ControlLabel>
                <FormControl
                    componentClass="select"
                    value={this.state.importModalDataFormat}
                    onChange={this.handleImportDataFormatChange.bind(this)}>
                  <option value="">Select...</option>
                  <option value="wellsfargo">Wells Fargo</option>
                  <option value="venmo">Venmo</option>
                  <option value="splitwise">Splitwise</option>
                </FormControl>
              </FormGroup>

              <FormGroup>
                <ControlLabel>File</ControlLabel>
                <FormControl type="file"
                    onChange={this.handleImportFileChange.bind(this)}/>
              </FormGroup>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button bsStyle="default" onClick={this.closeImportModal.bind(this)}>Cancel</Button>
            <Button bsStyle="primary" onClick={this.handleImport.bind(this)}>Import</Button>
          </Modal.Footer>
        </Modal>
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
  };
}

class EntryComponent extends Component {
  render() {
    const {date, description, amount, category} = this.props.entry;
    return (
      <tr>
        <td>{date}</td>
        <td>{description}</td>
        <td>{amount}</td>
        <td>{category}</td>
      </tr>
    )
  }
}

export default App;

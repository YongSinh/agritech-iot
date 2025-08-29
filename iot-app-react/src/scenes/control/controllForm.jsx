import React, { useEffect, useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  TextField,
  Grid,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl
} from "@mui/material";
const ControllForm = ({ open, handleClose, handleSubmit, sensors = [], initialData }) => {
  const booleans = ["on", "off"];
  const actions = ["sleep", "work", "valve", "lora-m-address", "lora-m-channel", "lora-s-address"];
  const valveDuration = [true, false];

  const [status, setStatus] = useState(false);
  const getInitialPayload = () => ({
    controlLogId: initialData?.id || "",
    valveDuration: "",
    state: "",
    sensor: "",
    type: ""
  });


  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
    setStatus(value === "valve")
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };


  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();
    // Convert Dayjs object to ISO string before submission if needed
    const submissionData = {
      ...formData
    };
    handleSubmit(submissionData);
  };
  const [formData, setFormData] = useState(getInitialPayload)
  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData(getInitialPayload());
    }
  }, [initialData]);

  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
      >
        <DialogTitle>Control Device</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new control log to the system.
          </DialogContentText>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="type-label">Type</InputLabel>
                <Select
                  labelId="type-label"
                  id="type"
                  fullWidth
                  name="type"
                  onChange={handleChange}
                  value={formData.type}
                  label="Type"
                >
                  {
                    actions.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
             <Grid item xs={6}>
              <FormControl required variant="outlined" fullWidth margin="dense">
                <InputLabel id="sensor-label">Sensor</InputLabel>
                <Select
                  labelId="sensor-label"
                  id="sensor"
                  fullWidth
                  name="sensor"
                  onChange={handleChange}
                  value={formData.sensor}
                  label="Sensor"
                >
                  {
                    sensors.map((value, index) => (
                      <MenuItem key={index} value={value?.value || ""}>
                        {value?.value || ""}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid> 
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="action-label">State</InputLabel>
                <Select
                  labelId="action-label"
                  id="state"
                  fullWidth
                  name="state"
                  onChange={handleChange}
                  value={formData.state}
                 // disabled={status}
                  label="State"
                >
                  {
                    booleans.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="valveDuration-label">Valve Duration</InputLabel>
                <Select
                  labelId="valveDuration-label"
                  id="valveDuration"
                  fullWidth
                  name="valveDuration"
                  onChange={handleChange}
                  value={formData.valveDuration}
                  label="Valve Duration"
                  // disabled={status}
                >
                  {
                    valveDuration.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value.toString()}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
           
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} variant="outlined" color="error" >Cancel</Button>
          <Button type="submit" variant="outlined" color="primary" >Save</Button>
        </DialogActions>
      </form>
    </Dialog>

  );
};

export default ControllForm;
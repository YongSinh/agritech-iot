import React, { useState, useEffect } from "react";
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
const ModelForm = ({ open, handleClose, handleSubmit, deviceIds, initialData }) => {

  const [formData, setFormData] = useState({
    id: initialData == null ? "" : initialData.id ,
    operator: "",
    sensor: "",
    value: "",
    action: "",
    duration: "",
    deviceId: ""
  });

  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    }
  }, [initialData]);

  // Handle form field changes
  const handleChange = (event) => {
    console.log(formData)
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault(); // Prevent page reload
    handleSubmit(formData); // Pass form data to the parent component
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <form onSubmit={onSubmit}>
        <DialogTitle>TRIGGER</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new trigger to the system.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="operator"
                name="operator"
                label="Operator"
                type="text"
                fullWidth
                onChange={handleChange}
                value={formData.operator}
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="sensor"
                name="sensor"
                label="Sensor"
                type="text"
                onChange={handleChange}
                value={formData.sensor}
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="value"
                name="value"
                label="Value"
                type="text"
                fullWidth
                onChange={handleChange}
                value={formData.value}
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="action"
                name="action"
                label="Action"
                type="text"
                onChange={handleChange}
                value={formData.action}
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="duration"
                name="duration"
                label="Duration"
                type="text"
                fullWidth
                onChange={handleChange}
                value={formData.duration}
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense">
                <InputLabel id="device-id-label">Device ID</InputLabel>
                <Select
                  labelId="device-id-label"
                  id="device-id"
                  name="deviceId"
                  value={formData.deviceId}
                  label="Device ID"
                  onChange={handleChange}
                  fullWidth
                >
                  {deviceIds.map((device, index) => (
                    <MenuItem key={index} value={device.deviceId}>
                      {device.deviceId}
                    </MenuItem>
                  ))}
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

export default ModelForm;
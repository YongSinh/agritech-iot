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
import { tokens } from "../../theme";
import { useTheme } from "@emotion/react";
const ModelForm = ({ open, handleClose, handleSubmit, initialData, masterDeviceName }) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const [formData, setFormData] = useState({
    deviceId: "",
    name: "",
    controller: "",
    sensors: "",
    remark: "",
    masterDeviceName: ""
  });

  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData(initialData);
    }
  }, [initialData]);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const onSubmit = (event) => {
    event.preventDefault(); // Prevent page reload
    handleSubmit(formData); // Pass form data to the parent component
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <form onSubmit={onSubmit}>
        <DialogTitle>IoT DEVICE</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new IoT device to the system.
          </DialogContentText>
          <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="deviceId"
                name="deviceId"
                label="Device ID"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.deviceId}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="name"
                name="name"
                label="Full Name"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.name}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="controller"
                name="controller"
                label="Controller"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.controller}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                // required
                margin="dense"
                id="sensors"
                name="sensors"
                label="Sensors"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.sensors}
                onChange={handleChange}
              />
            </Grid>
            <Grid item xs={12}>
              <FormControl fullWidth variant="outlined" margin="dense" required>
                <InputLabel id="masterDeviceName-label">Master Device Name</InputLabel>
                <Select
                  labelId="masterDeviceName-label"
                  id="masterDeviceName"
                  name="masterDeviceName"
                  value={formData.masterDeviceName}
                  onChange={handleChange}
                  label="Sensor"
                >
                  {masterDeviceName.map((name) => (
                    <MenuItem key={name} value={name}>
                      {name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <TextField
                margin="dense"
                id="remark"
                name="remark"
                label="Remark"
                type="text"
                fullWidth
                variant="outlined"
                multiline
                minRows={3}
                maxRows={10}
                value={formData.remark}
                onChange={handleChange}
              />
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
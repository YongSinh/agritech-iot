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
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs from "dayjs";

const ModelForm = ({ open, handleClose, handleSubmit, deviceIds, initialData }) => {
  const booleans = [true, false];

  const payload = {
    id: initialData == null ? "" : initialData.id,
    deviceId: "",
    dateTime: null,
    status: "",
    sentBy: "admin"
  }

  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle date/time change
  const handleDateTimeChange = (newValue) => {
    setFormData((prevData) => ({
      ...prevData,
      dateTime: newValue,
    }));
  };

  // Handle form submission
  const onSubmit = (event) => {
    event.preventDefault();
    // Convert Dayjs object to ISO string before submission if needed
    const submissionData = {
      ...formData,
      dateTime: formData.dateTime ? formData.dateTime.format('YYYY-MM-DDTHH:mm:ss') : null,
    };
    handleSubmit(submissionData);
  };
  const [formData, setFormData] = useState(payload)
  // Update form data when `initialData` changes
  useEffect(() => {
    if (initialData) {
      setFormData({
        ...initialData,
        // Convert string date to Dayjs object if needed
        dateTime: initialData.dateTime ? dayjs(initialData.dateTime) : null,
      });
    }
  }, [initialData]);

  return (
    <Dialog open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
      >
        <DialogTitle>CONTROL LOG</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please enter the details below to add a new control log to the system.
          </DialogContentText>
          <Grid container spacing={2}>
            <Grid item xs={6}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="status-label">Status</InputLabel>
                <Select
                  labelId="status-label"
                  id="status"
                  fullWidth
                  name="status"
                  onChange={handleChange}
                  value={formData.status}
                  label="Status"
                >
                  {
                    booleans.map((value, index) => (
                      <MenuItem key={index} value={value}>
                        {value.toString()}
                      </MenuItem>
                    ))
                  }
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <FormControl fullWidth variant="outlined" margin="dense">
                  <DateTimePicker
                    label="Run DateTime"
                    value={formData.dateTime}
                    onChange={handleDateTimeChange}
                    sx={{ width: '100%' }}
                    slotProps={{
                      textField: {
                        variant: 'outlined',
                      },
                    }}
                  />
                </FormControl>
              </LocalizationProvider>
            </Grid>
            <Grid item xs={6}>
              <TextField
                required
                margin="dense"
                id="duration"
                name="duration"
                label="Duration"
                type="number"
                onChange={handleChange}
                value={formData.duration}
                fullWidth
                variant="outlined"
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth variant="outlined" margin="dense">
                <InputLabel id="device-id-label">Device ID</InputLabel>
                <Select
                  labelId="device-id-label"
                  id="deviceId"
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
            <Grid item xs={12}>
              <TextField
                required
                margin="dense"
                id="sentBy"
                name="sentBy"
                label="Sent By"
                type="text"
                fullWidth
                variant="outlined"
                value={formData.sentBy}
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
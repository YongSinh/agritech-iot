import { useState } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Grid,
  Button,
  Select,
  MenuItem,
  InputLabel,
  FormControl
} from "@mui/material";

const ModelForm = ({ open, handleClose, handleSubmit, deviceIds = [], statusType }) => {
  const payload = {
    deviceId: "",
    status: ""
  }

  const [formData, setFormData] = useState(payload)

  // Handle form field changes
  const handleChange = (event) => {
    const { name, value } = event.target;
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


  return (
    <Dialog
      fullWidth={true}
      open={open} onClose={handleClose}>
      <form
        onSubmit={onSubmit}
      >
        <DialogTitle>Device Result</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Check device in result!
          </DialogContentText>
          <Grid container spacing={2}>
            <Grid item xs={12}>
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
                  {(deviceIds || []).map((device) => (
                    <MenuItem key={device.deviceId} value={device.deviceId}>
                      {device.deviceId}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12}>
              <FormControl variant="outlined" fullWidth margin="dense">
                <InputLabel id="status-select-label">Status</InputLabel>
                <Select
                  labelId="status-select-label"
                  id="status"
                  label="Status"
                  name="status"
                  onChange={handleChange}
                  value={formData.status}
                  fullWidth
                >
                  {(statusType || []).map((statusType, index) => (
                    <MenuItem key={index} value={statusType.status}>
                      {statusType.status}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} variant="outlined" color="error" >Cancel</Button>
          <Button type="submit" variant="outlined" color="primary" >Send</Button>
        </DialogActions>
      </form>
    </Dialog>

  );
};

export default ModelForm;
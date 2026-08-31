export default function StatCard({ label, value, icon: Icon, hint }) {
  return <div className="stat-card spotlight-card"><div className="stat-icon">{Icon&&<Icon size={20}/>}</div><div className="stat-copy"><span>{label}</span><strong>{value??0}</strong>{hint&&<small>{hint}</small>}</div></div>;
}

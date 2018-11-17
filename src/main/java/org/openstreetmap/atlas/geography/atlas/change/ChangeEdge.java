package org.openstreetmap.atlas.geography.atlas.change;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openstreetmap.atlas.geography.PolyLine;
import org.openstreetmap.atlas.geography.atlas.items.Edge;
import org.openstreetmap.atlas.geography.atlas.items.Node;
import org.openstreetmap.atlas.geography.atlas.items.Relation;

/**
 * @author matthieun
 */
public class ChangeEdge extends Edge // NOSONAR
{
    private static final long serialVersionUID = -5658471275390043045L;

    // At most one of those two can be null. Not using Optional here as it is not Serializable.
    private final Edge source;
    private final Edge override;

    protected ChangeEdge(final ChangeAtlas atlas, final Edge source, final Edge override)
    {
        super(atlas);
        this.source = source;
        this.override = override;
    }

    @Override
    public PolyLine asPolyLine()
    {
        return attribute(Edge::asPolyLine);
    }

    @Override
    public Node end()
    {
        return getChangeAtlas().node(endNodeIdentifier());
    }

    public long endNodeIdentifier()
    {
        return attribute(Edge::end).getIdentifier();
    }

    @Override
    public long getIdentifier()
    {
        return attribute(Edge::getIdentifier);
    }

    @Override
    public Map<String, String> getTags()
    {
        return attribute(Edge::getTags);
    }

    @Override
    public Set<Relation> relations()
    {
        return attribute(Edge::relations).stream()
                .map(relation -> getChangeAtlas().relation(relation.getIdentifier()))
                .collect(Collectors.toSet());
    }

    @Override
    public Node start()
    {
        return getChangeAtlas().node(startNodeIdentifier());
    }

    public long startNodeIdentifier()
    {
        return attribute(Edge::start).getIdentifier();
    }

    private <T extends Object> T attribute(final Function<Edge, T> memberExtractor)
    {
        return ChangeEntity.getAttributeOrBackup(this.source, this.override, memberExtractor);
    }

    private ChangeAtlas getChangeAtlas()
    {
        return (ChangeAtlas) getAtlas();
    }
}
